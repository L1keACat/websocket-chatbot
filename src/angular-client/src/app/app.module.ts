import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { MessageService } from "./service/message-service.service";
import {UserService} from "./service/user-service.service";
import { ChatComponent } from './chat/chat.component';
import {DataService} from "./service/data.service";
import { LoginComponent } from './login/login.component';
import {Resolver} from "./resolver";

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [MessageService, UserService, DataService, Resolver],
  bootstrap: [AppComponent]
})
export class AppModule { }
