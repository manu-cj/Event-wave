import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {ApiService} from '../api.service';
import { CommonModule, JsonPipe } from '@angular/common';


@Component({
  selector: 'app-joli-bouton',
  standalone: true,
  imports: [MatButtonModule, CommonModule, JsonPipe],
  templateUrl: './joli-bouton.html',
  styleUrl: './joli-bouton.css'
})
export class JoliBouton {
  data: any = null;
  loading = false;

  constructor(private api: ApiService) {}

  async charger() {
    this.loading = true;
    try {
      this.data = await this.api.getData();
    } finally {
      this.loading = false;
    }
  }
}
