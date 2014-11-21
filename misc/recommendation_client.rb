class RecommendationClient
  DEFAULT_SERVER = ENV["RECOM_ENGINE_SERVER"] || "localhost" #"womdev.reccomender.freelogue.net"
  DEFAULT_PORT   = ENV["RECOM_ENGINE_PORT"] || 2014
  def api_socket
    @@api_socket ||= TCPSocket.new(DEFAULT_SERVER,DEFAULT_PORT)
  end

  # def initialize(server, port)
  # @@server, @@port = server,port
  # end

  def send_request(request)
    request_thread = Thread.new {
      api_socket.print(request+"\n")
    }
    request_thread.join
  end

  def get_response()
    response_thread = Thread.new  {
        JSON.parse(api_socket.gets)   
    }
    response_thread.value  
  end

  def get_response_for_request(request)
  send_request(request)
  get_response
  end

  def get_recommendation_for_user(user_id,count=0)
    request = get_json_for_commend("getRecommendationForUser",{user_id:user_id,count:count})
    response = get_response_for_request(request)
    response["success"]? eval(response["response"]):[]
  end
    

  def get_json_for_commend(command,params)
    {command:command,params:params}.to_json
  end

end