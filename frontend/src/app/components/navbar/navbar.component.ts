import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {UserService} from '../../service/user.service';
import {IUser} from '../../models/user.model';
import {LucideAngularModule, Plus, Waves} from 'lucide-angular';


@Component({
  selector: 'app-navbar',
  standalone: true, //Permet d'utiliser le component directement
  templateUrl: './navbar.component.html',
  imports: [RouterModule, LucideAngularModule]
})
export class NavbarComponent implements OnInit {
  links: { label: string; path: string }[] = [
    { label: 'Home', path: '/' },
    { label: 'Events', path: '/events' },
    { label: 'Dashboard', path: '/dashboard' },
  ];
  username: string = '';
  isConnected: boolean = false;

  readonly waves = Waves;

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

  async onLogout() : Promise<void> {
    this.auth.clearToken();
    await this.router.navigate(['/login']);
  }

}
