import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {UserService} from '../../service/user.service';
import {IUser} from '../../models/user.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  imports: [RouterModule]
})
export class NavbarComponent implements OnInit {
  links = [
    { label: 'Accueil', path: '/dashboard' },
  ];
  username: string = '';

  isConnected: boolean = false;

  constructor(
    private api: UserService,
    private auth: AuthService,
    private router: Router,
  ) {}


  async ngOnInit(): Promise<void> {
    this.isConnected = !!this.auth.getToken();
    const token = localStorage.getItem('token');
    if (token) {
      this.api.getUserInfo(token).subscribe({
        next: async (result: IUser) => {
          if (result?.username && result?.email && result?.role) {
            this.username = result.username;
          }
        }
      });
    }
  }

  onLogout() {
    this.auth.clearToken();
  }
}
