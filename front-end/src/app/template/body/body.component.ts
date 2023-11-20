import { Component, Input } from '@angular/core';
import { AuthService } from 'src/app/seguranca/auth/auth.service';

@Component({
  selector: 'app-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.scss']
})
export class BodyComponent {

  @Input() collapsed = false;
  @Input() screenWidth = 0;

  constructor(public authService: AuthService) {}

  getBodyClass(): string {
    return this.collapsed ? 'body-trimmed' : '';
  }
}
