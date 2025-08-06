import {Component} from '@angular/core';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {ProfileData} from '../../components/profile-data/profileData.component';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.html',
  imports: [
    NavbarComponent,
    ProfileData
  ]
})

export class Profile {

}
