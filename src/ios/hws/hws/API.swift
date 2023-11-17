import Foundation

struct Channel: Codable {
    var channel_id: Int
    var name: String
    var is_admin: Bool
}

struct Event: Hashable, Codable {
    var event_id: Int
    var channel_id: Int
    var name: String
    var description: String
    var deadline: String
    var estimated: Double?
}

struct UserInfo: Codable {
    var channels: [Channel]
    var events: [Event]
}

struct Token: Codable {
    var token: String
}

enum API {
    static let host = "http://127.0.0.1:5000/"

    private static func makeRq(ep: String, parameters: [String: Any]?, method: String, token: String? = nil) async throws -> Data {
        var request = URLRequest(url: URL(string: API.host + ep)!)
        request.httpMethod = method
        if let parameters {
            let jsonData = try? JSONSerialization.data(withJSONObject: parameters, options: [])
            request.httpBody = jsonData
        }
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue(token, forHTTPHeaderField: "token")
        let (data, _) = try await URLSession.shared.data(for: request)
        return data
    }

    static func postUser(login: String, password: String) async throws -> String {
        let data = try await makeRq(ep: "user", parameters: ["login": login, "password": password], method: "POST")
        let response = try JSONDecoder().decode(Token.self, from: data)
        return response.token
    }
    
    static func getUserInfo(token: String?) async throws -> UserInfo {
        let data = try await makeRq(ep: "user/info", parameters: nil, method: "GET", token: token)
        let response = try JSONDecoder().decode(UserInfo.self, from: data)
        return response
    }

    static func postChannel(name: String, token: String?) async throws {
        _ = try await makeRq(ep: "channel", parameters: ["name": name], method: "POST", token: token)
    }

    static func postUserChannel(shortName: String, token: String?) async throws {
        _ = try await makeRq(ep: "user/channel", parameters: ["shortname": shortName], method: "POST", token: token)
    }

    static func deleteUserChannel(ch_id: String, token: String?) async throws {
        _ = try await makeRq(ep: "user/channel", parameters: ["ch_id": ch_id], method: "DELETE", token: token)
    }

    static func postChannelEvent(ch_id: String, name: String, descr: String, deadline: String, token: String?) async throws {
        _ = try await makeRq(ep: "channel/\(ch_id)/event", parameters: ["name": name,"descr": descr, "deadline": deadline], method: "POST", token: token)
    }

    static func postUserEvent(eventId: String, worktime: String, token: String?) async throws {
        _ = try await makeRq(ep: "user/event/\(eventId)", parameters: ["work_time": worktime], method: "POST", token: token)
    }

    
}
