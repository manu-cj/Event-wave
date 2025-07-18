import {Component, OnInit} from '@angular/core';
import { UserService } from '../../service/user.service';
import {Router} from '@angular/router';
import {IUser, IUserPage} from '../../models/user.model';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {Modal} from '../../components/modal/modal';
import {EventFormComponent} from '../../components/event-form/event-form';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    Modal,
    EventFormComponent
  ],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  user = {
    username: "",
    email: "",
    role: ""
  }
  userPage: IUserPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  isModalOpen: boolean = false;



  constructor(
    private api: UserService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    const token = localStorage.getItem('token');
    if (!token) {
      await this.router.navigate(['/login']);
      return;
    }

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
  }

  loadUsers() {
    this.api.getUsers(this.page, this.pageSize).subscribe({
      next: async (result: IUserPage) => {
        this.userPage = result;
      }
    });
  }

  nextPage() {
    this.page++;
    this.loadUsers();
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
    }
  }

  test() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }
}
