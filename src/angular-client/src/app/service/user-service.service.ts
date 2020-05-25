import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {

  private usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public findUser(username: String): Observable<User> {
    return this.http.get<User>(this.usersUrl+'/'+username);
  }

  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }
}
