import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {IEvent} from '../../models/event.model';
import {EventService} from '../../service/event.service';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.html',
  imports: [FormsModule]
})
export class EventFormComponent {
  @Input() token!: string;
  event: IEvent = {} as IEvent;
  selectedImage?: File;
  selectedFileName: string = 'No file selected';
  success = '';
  error = '';

  constructor(
    private eventApi: EventService,
  ) {}

  async onSubmit() {
    // Traitement du formulaire (ex : appel API)

    if (this.selectedImage) {
      const result : any = await this.eventApi.postEvent(this.event, this.token, this.selectedImage);
      if (result.status === 'success') {
        this.success = result.message;
        this.error = '';
        this.event = {} as IEvent;
        this.selectedImage = undefined;
      }
      else if (result.status === 'error') {
        this.error = result.message;
      }
    } else {
      this.error = 'No image selected';
    }
  }


  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.selectedImage = file;

      if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
        this.error = 'The image type is not allowed, upload a jpeg file or a png file.';
        this.selectedImage = undefined;
        return;
      }
      else {
        this.error = '';
        this.selectedFileName = file.name;
      }
    }
  }


}
