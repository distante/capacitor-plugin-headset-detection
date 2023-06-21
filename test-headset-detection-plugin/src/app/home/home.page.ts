import { Component, NgZone } from '@angular/core';
import { Plugins } from '@capacitor/core';
import {
  HeadsetDevice,
  HeadsetDetectionPlugin,
  HeadsetDetection,
  HeadsetTypes,
  HeadphoneDetectionEventNames,
} from '@saninn/capacitor-plugin-headset-detection';
import '@saninn/capacitor-plugin-headset-detection';
import { BehaviorSubject, Observable } from 'rxjs';
import { debounceTime, tap } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  public readonly HeadsetTypes = HeadsetTypes;
  private readonly isConnected$$ = new BehaviorSubject<boolean>(false);
  public readonly isConnected$: Observable<boolean> = this.isConnected$$.pipe(
    debounceTime(300),
    tap((isConnected) => console.log('HeadsetDetectionPlugin isConnected$', isConnected))
  );

  private readonly headsets$$ = new BehaviorSubject<HeadsetDevice | null>(null);
  public readonly headsets$: Observable<HeadsetDevice | null> = this.headsets$$.pipe(
    debounceTime(300),
    tap((headsets) => console.log('HeadsetDetectionPlugin headsets$', JSON.stringify(headsets)))
  );

  constructor(private readonly ngZone: NgZone) {
    HeadsetDetection.addListener(HeadphoneDetectionEventNames.ConnectedHeadphones, (info) => {
      this.ngZone.run(() => {
        this.headsets$$.next(info.device);
        this.isConnected$$.next(info.isConnected);
      });
    });

    HeadsetDetection.start();
  }

  public getObjectKeys(object: Record<string, any>): string[] {
    return Object.keys(object);
  }

  public alert(text: string): void {
    alert(text);
  }
}
