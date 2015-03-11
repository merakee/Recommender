class RecommendationClient
  DEFAULT_SERVER = ENV["RECOM_ENGINE_SERVER"] || "localhost" #"womdev.reccomender.freelogue.net"
  DEFAULT_PORT   = ENV["RECOM_ENGINE_PORT"] || 2014
  
  @@api_socket = nil
  
  def api_socket
    begin
      @@api_socket ||= TCPSocket.new(DEFAULT_SERVER,DEFAULT_PORT)
    rescue
      @@api_socket = nil 
    end
  end
  
  def is_socket_invalid 
    @@api_socket = nil if !@@api_socket.nil? && @@api_socket.closed? 
    api_socket
    @@api_socket.nil?
  end
  
  # def initialize(server, port)
  # @@server, @@port = server,port
  # end

  def send_request(request)
    request_thread = Thread.new {
      begin
        api_socket.print(request+"\n")
      rescue
        nil
      end
    }
    request_thread.join
  end

 def get_response()
    response_thread = Thread.new  {
      begin
      JSON.parse(api_socket.gets)
      rescue
      {}
      end
    }
    response_thread.value
  end

  def get_response_for_request(request)
  send_request(request)
  get_response
  end

  def get_recommendation_for_user(user_id, count=0)
    #return empty if there is problem with socket connection 
    return [] if is_socket_invalid 
    request = get_json_for_commend("getRecommendationForUser",{user_id:user_id,count:count})
    response = get_response_for_request(request)
    response["success"]? eval(response["response"]):[]
  end
    

  def get_json_for_commend(command,params)
    {command:command,params:params}.to_json
  end

end