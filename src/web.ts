import { registerWebPlugin, WebPlugin } from '@capacitor/core';

import { HeadsetDetectionPlugin } from './definitions';

export class HeadsetDetectionWeb extends WebPlugin implements HeadsetDetectionPlugin {
  constructor() {
    super({
      name: 'HeadsetDetection',
      platforms: ['web'],
    });

    
    // @ts-ignore
    if (!window['SSD']) {
      // @ts-ignore
      window['SSD'] = {}
    }
    // @ts-ignore
    if (!window['SSD'].plugins) {
    // @ts-ignore
      window['SSD'].plugins = {}
    }
      // @ts-ignore
    window['SSD'].plugins.headsetDetection = this;
  }

  start(): void {
    console.log('starting');
  }
}

const HeadsetDetection = new HeadsetDetectionWeb();

export { HeadsetDetection };

registerWebPlugin(HeadsetDetection);
