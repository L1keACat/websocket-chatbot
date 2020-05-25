import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import { Observable } from 'rxjs';
import { MessageService } from './service/message-service.service';
import {Message} from "./model/message";

@Injectable()
export class Resolver implements Resolve<Observable<Message[]>> {
  constructor(private api: MessageService) { }

  resolve(route: ActivatedRouteSnapshot) {
    return this.api.findUserMessages(route.params.username);
  }
}
