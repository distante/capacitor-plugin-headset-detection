import { WebPlugin } from '@capacitor/core';
import { RestartPlugin } from './definitions';

export class RestartWeb extends WebPlugin implements RestartPlugin {
  constructor() {
    super({
      name: 'Restart',
      platforms: ['web'],
    });
  }

  async restart(): Promise<void> {
    console.log('restarting');
    return;
  }
}

const Restart = new RestartWeb();

export { Restart };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Restart);
