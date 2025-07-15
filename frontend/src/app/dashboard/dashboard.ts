import {Component, OnInit} from '@angular/core';
import { ApiService } from '../api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  user = {
    username: "",
    email: "",
    role: ""
  }



  constructor(
    private api: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    this.api.getUserInfo(token)
      .then((result: any) => {
        if (result?.username && result?.email && result?.role) {
          this.user.username = result.username;
          this.user.email = result.email;
          this.user.role = result.role;
        } else {
          this.router.navigate(['/login']);
        }
      })
      .catch(() => {
        this.router.navigate(['/login']);
      });
  }
}
