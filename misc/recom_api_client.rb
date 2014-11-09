#!/usr/bin/env ruby -w
require 'socket'
require 'json' 

class Client
  def initialize( api_socket )
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
        #puts eval(jhash["response"])  if jhash["response"] 
      }
    end
  end
 
  def send
        @request = Thread.new do
      loop {
        msg = $stdin.gets.chomp
        @api_socket.print(msg + "\n")
      }
    end
  end
end
 
api_socket = TCPSocket.open( "localhost", 2014)
Client.new( api_socket )