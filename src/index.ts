import { registerPlugin } from '@capacitor/core';
import type { HeadsetDetectionPlugin } from './definitions';

const HeadsetDetection = registerPlugin<HeadsetDetectionPlugin>('HeadsetDetectionPlugin', {
  web: () => import('./web').then((m) => new m.HeadsetDetectionWeb()),
});

export * from './definitions';
export { HeadsetDetection };
