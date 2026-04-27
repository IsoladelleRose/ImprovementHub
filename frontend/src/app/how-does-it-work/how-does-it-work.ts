import { Component, AfterViewInit, OnDestroy, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

interface IconState {
  x: number;
  y: number;
  vx: number;
  vy: number;
  rotation: number;
  rotSpeed: number;
  timer: number;
}

@Component({
  selector: 'app-how-does-it-work',
  standalone: true,
  imports: [],
  templateUrl: './how-does-it-work.html',
  styleUrl: './how-does-it-work.scss'
})
export class HowDoesItWork implements AfterViewInit, OnDestroy {
  private rafId = 0;
  private states: IconState[] = [];

  constructor(private router: Router, private el: ElementRef) {}

  ngAfterViewInit() {
    this.startFloatingIcons();
  }

  ngOnDestroy() {
    cancelAnimationFrame(this.rafId);
  }

  private startFloatingIcons() {
    const container = this.el.nativeElement.querySelector('.floating-icons') as HTMLElement;
    if (!container) return;

    const icons = Array.from(container.querySelectorAll<HTMLElement>('.icon'));
    if (!icons.length) return;

    const rndSpeed = () => 0.4 + Math.random() * 0.9;
    const rndAngle = () => Math.random() * Math.PI * 2;
    const rndTimer = () => Math.floor(120 + Math.random() * 280);

    this.states = icons.map(() => {
      const w = container.offsetWidth || window.innerWidth;
      const h = container.offsetHeight || 500;
      const a = rndAngle();
      const s = rndSpeed();
      return {
        x: Math.random() * Math.max(w - 70, 0),
        y: Math.random() * Math.max(h - 70, 0),
        vx: Math.cos(a) * s,
        vy: Math.sin(a) * s,
        rotation: Math.random() * 360,
        rotSpeed: (Math.random() - 0.5) * 1.4,
        timer: rndTimer(),
      };
    });

    const loop = () => {
      const w = container.offsetWidth;
      const h = container.offsetHeight;

      this.states.forEach((state, i) => {
        const icon = icons[i];
        const iw = icon.offsetWidth || 55;
        const ih = icon.offsetHeight || 55;

        state.x += state.vx;
        state.y += state.vy;
        state.rotation += state.rotSpeed;
        state.timer--;

        if (state.x <= 0)        { state.x = 0;      state.vx =  Math.abs(state.vx); }
        if (state.x >= w - iw)   { state.x = w - iw; state.vx = -Math.abs(state.vx); }
        if (state.y <= 0)        { state.y = 0;      state.vy =  Math.abs(state.vy); }
        if (state.y >= h - ih)   { state.y = h - ih; state.vy = -Math.abs(state.vy); }

        if (state.timer <= 0) {
          const a = rndAngle();
          const s = rndSpeed();
          state.vx = Math.cos(a) * s;
          state.vy = Math.sin(a) * s;
          state.rotSpeed = (Math.random() - 0.5) * 1.4;
          state.timer = rndTimer();
        }

        icon.style.transform = `translate(${state.x}px, ${state.y}px) rotate(${state.rotation}deg)`;
      });

      this.rafId = requestAnimationFrame(loop);
    };

    this.rafId = requestAnimationFrame(loop);
  }

  navigateToPartnerRegistration() {
    this.router.navigate(['/partner-registration']);
  }

  navigateToIdeaRegistration() {
    this.router.navigate(['/idea-registration']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
