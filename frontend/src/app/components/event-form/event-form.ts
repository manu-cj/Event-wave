import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.html',
  imports: [FormsModule]
})
export class EventFormComponent {
  event = { name: '', date: '' };

  onSubmit() {
    // Traitement du formulaire (ex : appel API)
    console.log('Événement créé :', this.event);
  }
}
