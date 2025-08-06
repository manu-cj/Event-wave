import {Component, OnInit} from '@angular/core';
import {LucideAngularModule} from 'lucide-angular';
import {IUser} from '../../models/user.model';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-profile-data',
  imports: [LucideAngularModule],
  standalone: true,
  templateUrl: './profileData.component.html',
})

export class ProfileData implements OnInit {
  user: IUser = {} as IUser;

  constructor(
    private userApi: UserService,
  ) {}

  async ngOnInit(): Promise<void> {
    this.userApi.getUserInfo().subscribe({
      next: async (result: IUser) => {
        if (result?.username && result?.email && result?.role) {
          this.user = result;
        }
      },
      error: () => {
        this.user = {} as IUser;
      }
    })
  }

}
