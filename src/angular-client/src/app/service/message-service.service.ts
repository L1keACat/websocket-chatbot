import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Message } from '../model/message';
import { Observable } from 'rxjs';

@Injectable()
export class MessageService {

  private messagesUrl: string;

  constructor(private http: HttpClient) {
    this.messagesUrl = 'http://localhost:8080/messages';
  }

  public findAll(): Observable<Message[]> {
    return this.http.get<Message[]>(this.messagesUrl);
  }

  public findUserMessages(username: String): Observable<Message[]> {
    return this.http.get<Message[]>(this.messagesUrl+'/'+username);
  }

  public save(message: Message) {
    return this.http.post<Message>(this.messagesUrl, message);
  }
}
