import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule, FileIcon, X } from 'lucide-angular';
@Component({
  selector: 'app-modal',
  imports: [LucideAngularModule],
  templateUrl: './modal.html',
})
export class Modal {
  readonly FileIcon = FileIcon;
  readonly X = X;
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
