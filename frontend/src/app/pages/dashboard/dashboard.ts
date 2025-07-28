import {Component, OnInit} from '@angular/core';
import { UserService } from '../../service/user.service';
import {Router} from '@angular/router';
import {IUser, IUserPage} from '../../models/user.model';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {Modal} from '../../components/modal/modal';
import {UsersList} from '../../components/users-list/usersList.component';
import {EventFormComponent} from '../../components/event-form/event-form';
import {NgClass} from '@angular/common';
import {IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {EventsList} from '../../components/events-list/eventsList.component';
import {LucideAngularModule, Users, CalendarRange, Ticket, X, Plus} from 'lucide-angular';
import {ReservationsList} from '../../components/reservation-list/reservationList.component';


@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    Modal,
    EventFormComponent,
    UsersList,
    EventsList,
    NgClass,
    LucideAngularModule,
    ReservationsList
  ],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  user = {
    username: "",
    email: "",
    role: ""
  }
  viewMode: 'users' | 'events' | 'reservations' = 'users';
  userPage: IUserPage | null = null;
  eventPage: IEventPage = {} as IEventPage;
  page: number = 0;
  pageSize: number = 10;
  isModalOpen: boolean = false;
  token: string = '';

  readonly Users = Users;
  readonly CalendarRange = CalendarRange;
  readonly Ticket = Ticket;
  readonly Plus = Plus;



  constructor(
    private api: UserService,
    private router: Router,
    private eventApi: EventService,
  ) {}

  async ngOnInit(): Promise<void> {
    const token = localStorage.getItem('token');
    if (!token) {
      await this.router.navigate(['/login']);
      return;
    }
    this.token = token;
    this.api.getUserInfo(token).subscribe({
      next: async (result: IUser) => {
        if (result?.username && result?.email && result?.role) {
          this.user.username = result.username;
          this.user.email = result.email;
          this.user.role = result.role;
        } else {
          await this.router.navigate(['/login']);
        }
      },
      error: async () => {
        await this.router.navigate(['/login']);
      }
    });

    this.loadUsers();
    this.loadEvents();
  }

  loadUsers() : void {
    this.api.getUsers(this.page, this.pageSize).subscribe({
      next: async (result: IUserPage) => {
        this.userPage = result;
      }
    });
  }

  loadEvents() : void {
    this.eventApi.getEvents(this.page, this.pageSize).subscribe({
      next: async (result: IEventPage) => {
        this.eventPage = result;
      }
    })
  }

  nextPage() : void {
    this.page++;
    this.loadUsers();
    this.loadEvents();
  }

  prevPage() : void {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
      this.loadEvents();
    }
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  protected readonly X = X;
}
