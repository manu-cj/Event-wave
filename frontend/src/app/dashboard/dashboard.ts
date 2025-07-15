import {Component, OnInit} from '@angular/core';
import { ApiService } from '../api.service';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  username = '';
  email = '';
  role = '';


  constructor(
    private api: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.api.getUserInfo(token).then((result: any) => {
        if (result) {
          console.log(result);
          this.username = result.username;
          this.email = result.email;
          this.role = result.role;
        }
        else {
          this.router.navigate(['/login']);
        }
      }).catch((e) => {
        this.router.navigate(['/login']);
      });
    }
  }
}
