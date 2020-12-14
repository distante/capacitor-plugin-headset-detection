import { WebPlugin } from '@capacitor/core';
import { RestartPlugin } from './definitions';

export class RestartWeb extends WebPlugin implements RestartPlugin {
  constructor() {
    super({
      name: 'Restart',
      platforms: ['web'],
    });
  }

  start(): void {
    console.log('starting');
  }
}

const Restart = new RestartWeb();

export { Restart };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Restart);
