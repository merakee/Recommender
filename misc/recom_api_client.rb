#!/usr/bin/env ruby -w
require 'socket'
require 'json'

class Client
  def initialize( api_socket, mode="-m" )
    @mode = mode;
    puts "running auto mode...." if @mode.eql?"-a"
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
    {command:"getRecommendationForUser",params:user_id}.to_json
  end
  
  def  pause_for_random_time
    sleep rand(15)
  end
end

api_socket = TCPSocket.open( "localhost", 2014)
Client.new( api_socket,ARGV[0])