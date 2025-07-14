import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {JoliBouton} from './joli-bouton/joli-bouton';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JoliBouton],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
