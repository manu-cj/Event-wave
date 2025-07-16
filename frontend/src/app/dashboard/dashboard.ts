import {Component, OnInit} from '@angular/core';
import { UserService } from '../service/user.service';
import {Router} from '@angular/router';
import {IUser} from '../models/user.model';
import {NavbarComponent} from '../components/navbar/navbar.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent
  ],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  user = {
    username: "",
    email: "",
    role: ""
  }



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
  }
}
