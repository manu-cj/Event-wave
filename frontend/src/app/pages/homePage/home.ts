import {Component, OnInit, signal} from '@angular/core';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {EventCard} from '../../components/event-card/eventCard.component';
import {IEvent} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {IUser} from '../../models/user.model';
import {AuthService} from '../../service/auth.service';
import {UserService} from '../../service/user.service';
import {Router, RouterLink} from '@angular/router';



@Component({
  selector: 'app-root',
  imports: [NavbarComponent, EventCard, RouterLink],
  templateUrl: './home.html',


})
export class HomePage implements OnInit {
  protected readonly title = signal('frontend');

  lastEvents : IEvent[] = [];
  isConnected: boolean = false;
  username: string = '';

  constructor(
    private eventApi: EventService,
    private auth: AuthService,
    private userApi: UserService,
    private router: Router,
  ) {}

  loadLastEvents() : void {
    this.eventApi.getLastEvents().subscribe({
      next: async (result: IEvent[])=> {
        this.lastEvents = result;
        console.log(result);
      }
    })
  }
  ngOnInit(): void {
    this.isConnected = !!this.auth.getToken();
    const token = localStorage.getItem('token');
    if (token) {
      this.userApi.getUserInfo(token).subscribe({
        next: async (result: IUser) => {
          if (result?.username && result?.email && result?.role) {
            this.username = result.username;
          }
        }
      });
    }
    this.loadLastEvents();

  }
}
