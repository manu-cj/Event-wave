import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {FormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import {ILogin} from '../../models/user.model';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {
  email = '';
  password = '';
  error = '';
  success = '';

  constructor(
    private api: UserService,
    private auth: AuthService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.api.getUserInfo().subscribe({
      next: async () => {
        await this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.error = 'You are not connected. Please login or register.';
      }
    });
  }

  async onLogin() {
    try {
      const user: ILogin = { email: this.email, password: this.password };
      const result: any = await this.api.login(user);
      if (result && result.token) {
        this.error = '';
        this.success = "Authentification with success";
        await this.router.navigate(['/dashboard']);
      } else {
        this.error = 'Authentification failed';
      }
    } catch (e) {
      this.error = 'Error when logging in :';
    }
  }
}
