enum MessageType {
  CHAT,
  JOIN,
  LEAVE
}

export class Message {
  id: string;
  type: MessageType;
  content: string;
  sender: string;
  userName: string;
}
