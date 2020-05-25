import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChatComponent } from "./chat/chat.component";
import {LoginComponent} from "./login/login.component";
import {Resolver} from "./resolver";

const routes: Routes = [
  { path: 'chat/:username', component: ChatComponent, resolve: { messages: Resolver } },
  { path: '', component: LoginComponent }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
