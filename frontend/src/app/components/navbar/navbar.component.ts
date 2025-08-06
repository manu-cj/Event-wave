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
  ];
  username: string = '';
  isAdmin: boolean = false;
  isConnected: boolean = false;

  readonly waves = Waves;

  constructor(
    private api: UserService,
    private auth: AuthService,
    private router: Router,
  ) {}


  async ngOnInit(): Promise<void> {
    this.api.getUserInfo().subscribe({
      next: (result: IUser) => {
        if (result?.username && result?.email && result?.role) {
          this.username = result.username;
          this.isAdmin = result.role === 'ADMIN';
          this.isConnected = true;
        } else {
          this.isConnected = false;
        }
      },
      error: () => {
        this.isConnected = false;
      }
    });
  }

   async onLogout() {
    await this.api.logout();
    this.isConnected = false;
    await this.router.navigate(['/login']);
  }

}
