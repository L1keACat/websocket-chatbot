import {Component, ElementRef, ViewChild, Renderer2, OnInit, AfterViewInit} from '@angular/core';
import { Message } from '../model/message';
import { MessageService } from '../service/message-service.service';
import { User } from '../model/user';
import { UserService } from '../service/user-service.service';
import { DataService } from '../service/data.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {ActivatedRoute, Router} from "@angular/router";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewInit {

  private serverUrl = 'http://localhost:8080/ws'
  private stompClient;

  messages: Message[];
  bookmarks;
  username;

  colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
  ];

  @ViewChild('usernamePage') usernamePage: ElementRef;
  @ViewChild('chatPage') chatPage: ElementRef;
  @ViewChild('usernameForm') usernameForm: ElementRef;
  @ViewChild('messageForm') messageForm: ElementRef;
  @ViewChild('message') messageInput: ElementRef;
  @ViewChild('messageArea') messageArea: ElementRef;
  @ViewChild('connecting') connectingElement: ElementRef;
  @ViewChild('buttonGroup') buttonGroup: ElementRef;
  @ViewChild('typing') typing: ElementRef;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private userService: UserService,
    private renderer: Renderer2,
    private data: DataService,
    private elem: ElementRef) {
  }

  ngOnInit() {
    this.data.currentMessage.subscribe(message => this.username = message)
    if (!this.username) {
      this.router.navigate(['/']);
    }
    if(this.username) {
      const socket = new SockJS(this.serverUrl);
      this.stompClient = Stomp.over(socket);
      const _this = this;
      _this.stompClient.connect({}, function (frame) {
          _this.stompClient.subscribe('/topic/public/'+_this.username, function (sdkEvent) {
            _this.onMessageReceived(sdkEvent);
          });
          _this.stompClient.send("/app/chat.addUser",
            {},
            JSON.stringify({sender: _this.username, type: 'JOIN', userName: _this.username})
          )
          //_this.stompClient.reconnect_delay = 2000;
          _this.connectingElement.nativeElement.classList.add('hidden');
        },
        _this.onError);
      /*this.messageService.findUserMessages(this.username).subscribe(data => {
        this.messages = data;
      });*/
      this.messages = this.route.snapshot.data.messages;
    }
    this.messageArea.nativeElement.scrollTop = this.messageArea.nativeElement.scrollHeight;
  }

  ngAfterViewInit() {
    let elements = this.elem.nativeElement.querySelectorAll('.chat-message');
    const _this = this;
    elements.forEach(function (element) {
      if (element.children[2].innerHTML.startsWith("24-hour forecast for")) {
        _this.createWeatherForecast(element.children[2].innerHTML, element.children[2]);
      }
    })
  }


  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
    this.router.navigate(['/']);
  }

  onError() {
    this.connectingElement.nativeElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    this.connectingElement.nativeElement.style.color = 'red';
  }


  sendMessage(messageContent) {
    if(messageContent && this.stompClient) {
      let chatMessage = {
        sender: this.username,
        content: this.messageInput.nativeElement.value,
        type: 'CHAT',
        userName: this.username
      };
      this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
      this.messageInput.nativeElement.value = '';
    }
    event.preventDefault();
  }

  onMessageReceived(payload) {
    let m = JSON.parse(payload.body);

    let messageElement = this.renderer.createElement('li');

    if (m.sender == "Bot")
      this.typing.nativeElement.classList.add("hidden");
    else
      this.typing.nativeElement.classList.remove("hidden");
    if (m.type === 'JOIN') {
      messageElement.classList.add('event-message');
      m.content = m.sender + ' joined!';
    } else if (m.type === 'LEAVE') {
      messageElement.classList.add('event-message');
      m.content = m.sender + ' left!';
    } else {
      messageElement.classList.add('chat-message');

      let avatarElement = this.renderer.createElement('i');
      let avatarText = this.renderer.createText(m.sender[0]);
      this.renderer.appendChild(avatarElement, avatarText);
      avatarElement.style['background-color'] = this.getAvatarColor(m.sender);

      this.renderer.appendChild(messageElement, avatarElement);

      let usernameElement = this.renderer.createElement('span');
      let usernameText = this.renderer.createText(m.sender);
      this.renderer.appendChild(usernameElement, usernameText);
      this.renderer.appendChild(messageElement, usernameElement);
    }

    let textElement = this.renderer.createElement('p');
    let messageText;
    if (!m.content.includes("24-hour forecast for") && m.content.includes('icon:')) {
      let textElement_inner = this.renderer.createElement('p');
      let ind = m.content.indexOf(':');
      let place = m.content.substring(0, ind + 1);
      let messageText_place = this.renderer.createText(place);
      this.renderer.appendChild(textElement_inner, messageText_place);
      let ind_img = m.content.indexOf('icon:') + 6;
      let img_icon = m.content.substring(ind_img, ind_img + 3);
      let img = this.renderer.createElement('img');
      this.renderer.setAttribute(img,"src","http://openweathermap.org/img/wn/" + img_icon + "@2x.png");
      this.renderer.setStyle(img, "height", "4em");
      messageText = this.renderer.createText(m.content.substring(ind_img + 5));
      this.renderer.appendChild(textElement, textElement_inner);
      this.renderer.appendChild(textElement, img);
      this.renderer.appendChild(textElement, messageText);
    }
    else if (m.content.includes("24-hour forecast for")) {
      this.createWeatherForecast(m.content, textElement);
    }
    else {
      messageText = this.renderer.createText(m.content);
      this.renderer.appendChild(textElement, messageText);
    }

    this.renderer.appendChild(messageElement, textElement);

    let options = [];

    this.userService.findUser(m.userName).subscribe(data => {
      if (data)
        this.bookmarks = data.bookmarks;
    });

    this.buttonGroup.nativeElement.innerHTML='';
    let buttonElement = this.renderer.createElement('p');
    if (m.content.endsWith("How can I help you?") || m.content.endsWith("Anything else?") || m.content.startsWith("The result is")) {
      options = ["Compute", "Timezone", "Weather"];
    }
    if (m.content.endsWith("What type?")) {
      options = ["Current", "24-hour forecast"];
    }
    if (m.content.endsWith("Where?")) {
      options = this.bookmarks;
      if (this.bookmarks.length != 0)
        options.push("Edit");
    }
    if (m.content.endsWith("Which one do you want to delete?")) {
      options = this.bookmarks;
      options.push("Back");
    }
    if (m.content.endsWith("Please check and try again.") || m.content.endsWith("Write the expression")) {
      options.push("Back");
    }
    if (m.content.endsWith("Do you want to save this location?")) {
      options = ["Yes", "No"];
    }
    if (options.length != null) {
      for (let i = 0; i < options.length; i++) {
        let button = this.renderer.createElement("button");
        button.classList.add('button-message');
        button.innerHTML = options[i];
        this.renderer.listen(button, 'click', (evt) => {
          this.press(evt.target.innerHTML);
        });
        this.renderer.appendChild(buttonElement, button);
        this.buttonGroup.nativeElement.appendChild(buttonElement);
      }
    }

    this.messageArea.nativeElement.appendChild(messageElement);
    this.messageArea.nativeElement.scrollTop = this.messageArea.nativeElement.scrollHeight;

  }

  press(innerhtml){
    if(this.stompClient) {
      let chatMessage = {
        sender: this.username,
        content: innerhtml,
        type: 'CHAT',
        userName: this.username
      };
      this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
    }
  }

  getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
    }
    let index = Math.abs(hash % this.colors.length);
    return this.colors[index];
  }

  createWeatherForecast(data, textElement) {
    textElement.innerHTML = "";
    let save_q;
    if (data.endsWith("Do you want to save this location?")) {
      save_q = data.substring(data.indexOf("Do you want to save this location?"));
    }
    //------------------------header--------------------------
    let header = this.renderer.createElement('p');
    let ind = data.indexOf(':');
    let text = data.substring(0, ind + 1);
    let header_text = this.renderer.createText(text);
    this.renderer.appendChild(header, header_text);
    this.renderer.appendChild(textElement, header);
    data = data.substring(ind + 1);
    //------------------------scrollmenu--------------------------
    let scrollmenu = this.renderer.createElement('div');
    scrollmenu.classList.add('scrollmenu');
    this.renderer.appendChild(textElement, scrollmenu);
    //------------------------menu_elements--------------------------
    for (let i = 0; i < 9; i++) {
      let w_el = this.renderer.createElement('div');
      w_el.classList.add('w_el');
      let end_ind = data.indexOf('---');
      let el = data.substring(0, end_ind);
      let ind1 = el.indexOf(': ');
      let time = this.renderer.createElement('p');
      let time_text = this.renderer.createText(el.substring(0, ind1).trim());
      this.renderer.appendChild(time, time_text);
      this.renderer.appendChild(w_el, time);
      let ind_img = el.indexOf('icon:') + 6;
      let img_icon = el.substring(ind_img, ind_img + 3);
      let img = this.renderer.createElement('img');
      this.renderer.setAttribute(img,"src","http://openweathermap.org/img/wn/" + img_icon + "@2x.png");
      this.renderer.setStyle(img, "height", "4em");
      this.renderer.appendChild(w_el, img);
      let ind_temp = el.indexOf('Temp:') + 6;
      let ind_C = el.indexOf('°C');
      let w_info = this.renderer.createElement('p');
      let w_info_text = el.substring(ind_temp, ind_C + 2);
      let w_info_text_rend = this.renderer.createText(w_info_text);
      this.renderer.appendChild(w_info, w_info_text_rend);
      this.renderer.appendChild(w_el, w_info);
      this.renderer.appendChild(scrollmenu, w_el);
      data = data.substring(end_ind + 3);
    }
    if (save_q != null) {
      let save = this.renderer.createElement('p');
      let save_text = this.renderer.createText(save_q);
      this.renderer.appendChild(save, save_text);
      this.renderer.appendChild(textElement, save);
    }
  }
}
