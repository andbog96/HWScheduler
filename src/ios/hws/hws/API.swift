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

enum API {
    static func postUser(login: String, password: String) async throws -> String {
        "qwe"
    }
    
    static func getUserInfo(token: String) async throws -> UserInfo {
        UserInfo(
            channels: [
                Channel(channel_id: 1, name: "ch1", is_admin: true),
                Channel(channel_id: 2, name: "ch2", is_admin: false)
            ],
            events: [
                Event(
                    event_id: 1,
                    channel_id: 1,
                    name: "Ev 1",
                    description: "qweqwe",
                    deadline: "222",
                    estimated: nil
                ),
                Event(
                    event_id: 2,
                    channel_id: 2,
                    name: "Ev 2",
                    description: "asdasd",
                    deadline: "111",
                    estimated: 0
                )
            ]
        )
    }
}
