import { Component, inject, OnInit, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { LANGUAGES } from '../../config/language.constants';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { ProfileService } from '../../layouts/profiles/profile.service';
import TranslateDirective from '../../shared/language/translate.directive';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap';
import { ClaimService } from '../../entities/insurance/claim/service/claim.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'jhi-admin-sidebar',
  imports: [FaIconComponent, RouterLink, RouterLinkActive, TranslateDirective, NgbCollapse, NgIf],
  templateUrl: './admin-sidebar.component.html',
  styleUrl: './admin-sidebar.component.scss',
})
export class AdminSidebarComponent implements OnInit {
  inProduction?: boolean;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  isCollapsed = false;
  isGestionOpen = false;
  pendingClaimCount = signal(0);

  private readonly stateStorageService = inject(StateStorageService);
  private readonly profileService = inject(ProfileService);
  private readonly router = inject(Router);
  private readonly claimService = inject(ClaimService);

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.loadPendingClaimsCount();
  }

  toggleSidebar(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  toggleGestion(): void {
    this.isGestionOpen = !this.isGestionOpen;
  }

  loadPendingClaimsCount(): void {
    this.claimService
      .query({
        'status.equals': 'IN_PROGRESS',
        size: 1000,
      })
      .subscribe({
        next: res => {
          this.pendingClaimCount.set(res.body?.length ?? 0);
        },
        error: () => {
          this.pendingClaimCount.set(0);
        },
      });
  }
}
