import { Component, inject, OnInit } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import TranslateDirective from '../../shared/language/translate.directive';
import { LANGUAGES } from '../../config/language.constants';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { ProfileService } from '../../layouts/profiles/profile.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'jhi-client-sidebar',
  imports: [FaIconComponent, RouterLink, RouterLinkActive, TranslateDirective],
  templateUrl: './client-sidebar.component.html',
  styleUrl: './client-sidebar.component.scss',
})
export class ClientSidebarComponent implements OnInit {
  inProduction?: boolean;
  isCollapsed: boolean = false;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';

  private readonly stateStorageService = inject(StateStorageService);
  private readonly profileService = inject(ProfileService);
  private readonly router = inject(Router);

  constructor() {
    const { VERSION } = environment;
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    // Subscribe to language changes
  }

  toggleSidebar(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
