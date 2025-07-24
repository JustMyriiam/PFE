import { Component, inject, OnInit } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { LANGUAGES } from '../../config/language.constants';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { ProfileService } from '../../layouts/profiles/profile.service';
import { environment } from '../../../environments/environment';
import TranslateDirective from '../../shared/language/translate.directive';
import HasAnyAuthorityDirective from '../../shared/auth/has-any-authority.directive';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-admin-sidebar',
  imports: [FaIconComponent, RouterLink, RouterLinkActive, TranslateDirective, HasAnyAuthorityDirective, NgbCollapse],
  templateUrl: './admin-sidebar.component.html',
  styleUrl: './admin-sidebar.component.scss',
})
export class AdminSidebarComponent implements OnInit {
  inProduction?: boolean;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  isCollapsed = false;
  isGestionOpen = false;

  private readonly stateStorageService = inject(StateStorageService);
  private readonly profileService = inject(ProfileService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  toggleSidebar(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  toggleGestion(): void {
    this.isGestionOpen = !this.isGestionOpen;
  }
}
