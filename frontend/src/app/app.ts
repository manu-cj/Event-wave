import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DatePipe, NgFor } from '@angular/common';
import {NavbarComponent} from './components/navbar/navbar.component';
import {EventCard} from './components/event-card/eventCard.component';
import {IEvent} from './models/event.model';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, DatePipe, NgFor, NavbarComponent, EventCard],
  templateUrl: './app.html',
  styleUrls: ['./app.css']

})
export class App {
  protected readonly title = signal('frontend');

  // Exemple de données mock, à remplacer par un appel à votre service
  lastEvents : IEvent[] = [
    {
      title: "Hello",
      description: "Description de l'événement",
      date: new Date(),
      city: "Charleroi",
      postalCode: 6000,
      address: "1 rue Exemple",
      availablePlaces: 7,
      pictureUrl: "/uploads/0b4e7dc2-c5c7-4f72-ac3d-e235c26768ef_Design sans titre.png"
    },
    {
      title: "Hello",
      description: "Description de l'événement",
      date: new Date(),
      city: "Charleroi",
      postalCode: 6000,
      address: "1 rue Exemple",
      availablePlaces: 7,
      pictureUrl: "/uploads/0b4e7dc2-c5c7-4f72-ac3d-e235c26768ef_Design sans titre.png"
    },
    {
      title: "Hello",
      description: "Description de l'événement",
      date: new Date(),
      city: "Charleroi",
      postalCode: 6000,
      address: "1 rue Exemple",
      availablePlaces: 7,
      pictureUrl: "/uploads/0b4e7dc2-c5c7-4f72-ac3d-e235c26768ef_Design sans titre.png"
    }

  ];
}
