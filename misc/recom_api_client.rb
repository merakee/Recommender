#!/usr/bin/env ruby -w
require 'socket'
require 'json'

class Client
  def initialize( api_socket, mode="-m" )
    @mode = mode;
    @api_socket = api_socket
    @request = nil
    @response = nil
    listen
    send
    @request.join
    @response.join
  end

  def listen
    @response = Thread.new do
      loop {
        msg = @api_socket.gets
        jhash =  JSON.parse(msg)
        puts jhash
      }
    end
  end


  def send
    @request = Thread.new do
      loop {
        msg = get_user_input
        puts msg 
        @api_socket.print(msg+"\n")
      }
    end
  end
  
  def get_user_input 
    if(@mode.eql?"-a")
      pause_for_random_time
      user_id =  rand(100)
    else
       user_id = $stdin.gets.chomp.to_i
    end
    
    return get_command(user_id) 
  end
  
  def get_command(user_id=0)
    {command:"getRecommendationForUser",params:{user_id:user_id,count:10}}.to_json
  end
  
  def  pause_for_random_time
    sleep rand(120)
  end
end

host = (ARGV[1].eql?"-aws")?"womdev.recommender.freelogue.net":"localhost"
puts "Connection to #{host} ...running #{(ARGV[0].eql?"-a")?"auto":"manual"} mode...." 

begin
  api_socket = TCPSocket.open( host, 2014)
rescue => e
  puts e.message
  exit
end

Client.new( api_socket,ARGV[0])