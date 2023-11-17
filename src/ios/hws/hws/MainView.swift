import SwiftUI

struct MainView: View {
    @State private var login = ""
    @State private var password = ""
    @State private var loginAlert = false
    @State private var token = nil as String?

    var body: some View {
        if token != nil {
            mainView
        } else {
            signView
        }
    }

    var signView: some View {
        NavigationStack {
            VStack {
                Text("Вход")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.bottom, 20)
                TextField("Имя пользователя", text: $login)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                    .textInputAutocapitalization(.never)
                SecureField("Пароль", text: $password)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                Button(action: {
                    Task {
                        do {
                            token = try await API.postUser(login: login, password: password)
                        } catch {
                            loginAlert = true
                        }
                    }
                }) {
                    Text("Войти")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding()
                        .frame(width: 220, height: 60)
                        .background(Color.blue)
                        .cornerRadius(15.0)
                }
            }
            .padding()
            .alert(isPresented: $loginAlert, content: {
                Alert(
                    title: Text("Ошибка"),
                    message: Text("Неправильный пароль"),
                    dismissButton: .default(Text("Понял"))
                )
            })
        }
    }

    var mainView: some View {
        TabView {
            eventsView
                .tabItem { Label("ДЗ", systemImage: "list.dash") }
            profileView
                .tabItem { Label("Профиль", systemImage: "person") }
        }
    }

    @State var events = [] as [Event]
    @State var channels = [] as [Channel]
    @State var completeAlert = false
    @State var completeEvent = nil as Event?
    @State var completeTime = ""
    @State var subscribeAlert = false
    @State var subscribeChName = ""
    @State var makeChAlert = false
    @State var makeChName = ""

    var eventsView: some View {
        NavigationView {
            List {
                ForEach(events, id: \.self) { event in
                    NavigationLink(event.name) {
                        eventDescriptionView(event: event)
                    }
                    .swipeActions(edge: .leading, allowsFullSwipe: false) {
                        Button(
                            action: {
                                completeAlert = true
                                completeEvent = event
                            },
                            label: {
                                Image(systemName: "checkmark")
                            }
                        )
                        .tint(.green)
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarTitle("Домашки")
            .navigationBarItems(leading: Button(
                action: {
                    subscribeAlert = true
                },
                label: {
                    Image(systemName: "plus")
                }
            ))
        }
        .alert(
            Text("Время"),
            isPresented: $completeAlert,
            actions: {
                TextField("Сколько минут ушло на дз", text: $completeTime)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                    .keyboardType(.numberPad)
                Button("Отмена", role: .cancel, action: {})
                Button("Отправить") {
                    Task {
                        try? await API.postUserEvent(eventId: String(completeEvent!.event_id), worktime: completeTime, token: token)
                        events = (try? await API.getUserInfo(token: token).events) ?? []
                    }
                    events.removeAll(where: { $0.event_id == completeEvent?.event_id })
                }
            }
        )
        .alert(
            Text(""),
            isPresented: $subscribeAlert,
            actions: {
                TextField("Имя канала", text: $subscribeChName)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                    .keyboardType(.numberPad)
                Button("Отмена", role: .cancel, action: {})
                Button("Подписаться") {
                    Task {
                        try? await API.postUserChannel(shortName: subscribeChName, token: token)
                        channels = (try? await API.getUserInfo(token: token))?.channels ?? []
                    }
                }
            }
        )
        .task {
            events = (try? await API.getUserInfo(token: token).events) ?? []
        }
        .refreshable {
            events = (try? await API.getUserInfo(token: token).events) ?? []
        }
    }

    var profileView: some View {
        NavigationView {
            List(channels, id: \.channel_id) { channel in
                if channel.is_admin {
                    NavigationLink(channel.name) {
                        chEventsView(ch: channel)
                    }
                } else {
                    VStack(alignment: .leading) {
                        Text(channel.name)
                            .font(.headline)
                    }
                    .swipeActions(edge: .leading, allowsFullSwipe: false) {
                        Button(
                            action: {
                                Task {
                                   try await API.deleteUserChannel(ch_id: String(channel.channel_id), token: token)
                                    channels = (try? await API.getUserInfo(token: token))?.channels ?? []
                                }
                            },
                            label: {
                                Image(systemName: "trash")
                            }
                        )
                        .tint(.red)
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarTitle("Профиль")
            .navigationBarItems(
                leading: Button(
                    action: {
                        token = nil
                    },
                    label: {
                        Image(systemName: "door.left.hand.open")
                    }
                ),
                trailing: Button(
                    action: {
                        makeChAlert = true
                    },
                    label: {
                        Image(systemName: "plus")
                    }
                )
            )
        }
        .alert(
            Text(""),
            isPresented: $makeChAlert,
            actions: {
                TextField("Имя канала", text: $makeChName)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                    .keyboardType(.numberPad)
                Button("Отмена", role: .cancel, action: {})
                Button("Создать") {
                    Task {
                        try? await API.postChannel(name: makeChName, token: token)
                        channels = (try? await API.getUserInfo(token: token))?.channels ?? []
                    }
                }
            }
        )
        .task {
            channels = (try? await API.getUserInfo(token: token))?.channels ?? []
        }
        .refreshable {
            channels = (try? await API.getUserInfo(token: token))?.channels ?? []
        }
    }

    func eventDescriptionView(event: Event) -> some View {
        NavigationStack {
            VStack {
                Text(event.name)
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.bottom, 20)
                Text("Дедлайн: \(event.deadline)")
                Text(event.description)
            }
            .padding()
        }
    }

    @State var removeAlert = false
    @State var removeEvent = nil as Event?
    @State var mkEvAlert = false
    @State var mkEvCh = ""
    @State var mkEvName = ""
    @State var mkEvDeadline = ""
    @State var mkEvDescr = ""

    func chEventsView(ch: Channel) -> some View {
        List {
            ForEach(events.filter { $0.channel_id == ch.channel_id }, id: \.self) { event in
                NavigationLink(event.name) {
                    eventDescriptionView(event: event)
                }
                .swipeActions(edge: .leading, allowsFullSwipe: false) {
                    Button(
                        action: {
                            removeAlert = true
                            removeEvent = event
                        },
                        label: {
                            Image(systemName: "trash")
                        }
                    )
                    .tint(.red)
                }
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarTitle("Домашки")
        .navigationBarItems(trailing: Button(
            action: {
                mkEvAlert = true
            },
            label: {
                Image(systemName: "plus")
            }
        ))
        .alert(
            Text("Удалить"),
            isPresented: $removeAlert,
            actions: {
                Button("Отмена", role: .cancel, action: {})
                Button("Удалить") {
                    events.removeAll(where: { $0.event_id == removeEvent?.event_id })
                }
            }
        )
        .alert(
            Text("Создать дз"),
            isPresented: $mkEvAlert,
            actions: {
                TextField("Канал", text: $mkEvCh)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                TextField("Название дз", text: $mkEvName)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                TextField("Дедлайн", text: $mkEvDeadline)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                TextField("Описание", text: $mkEvDescr)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(5.0)
                    .padding(.bottom, 20)
                Button("Отмена", role: .cancel, action: {})
                Button("Отправить") {
                    Task {
                        try? await API.postChannelEvent(ch_id: mkEvCh, name: mkEvName, descr: mkEvDescr, deadline: mkEvDeadline, token: token)
                        events = (try? await API.getUserInfo(token: token).events) ?? []
                    }
                }
            }
        )
        .task {
            events = (try? await API.getUserInfo(token: token).events) ?? []
        }
        .refreshable {
            events = (try? await API.getUserInfo(token: token).events) ?? []
        }
    }
}

#Preview {
    MainView()
}
