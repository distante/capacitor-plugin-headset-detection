import { WebPlugin } from '@capacitor/core';
import { HeadsetDetectionPlugin } from './definitions';

export class HeadsetDetectionWeb extends WebPlugin implements HeadsetDetectionPlugin {
  constructor() {
    super({
      name: 'HeadsetDetection',
      platforms: ['web'],
    });
  }

  start(): void {
    console.log('starting');
  }
}

const HeadsetDetection = new HeadsetDetectionWeb();

export { HeadsetDetection };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(HeadsetDetection);
