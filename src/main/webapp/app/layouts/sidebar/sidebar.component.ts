import { Component, inject, OnInit, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { LANGUAGES } from '../../config/language.constants';
import { environment } from '../../../environments/environment';
import { ProfileService } from '../profiles/profile.service';
import { StateStorageService } from '../../core/auth/state-storage.service';
import TranslateDirective from '../../shared/language/translate.directive';

@Component({
  selector: 'jhi-sidebar',
  imports: [FaIconComponent, RouterLinkActive, RouterLink, TranslateDirective],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export default class SidebarComponent implements OnInit {
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
