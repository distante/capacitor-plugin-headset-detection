import { PluginListenerHandle } from '@capacitor/core';
import { HeadsetTypes, HeadphoneDetectionEventNames } from './models';
declare module '@capacitor/core' {
  interface PluginRegistry {
    HeadsetDetection: HeadsetDetectionPlugin;
  }
}

export interface HeadsetDevice {
  id: number;
  type: HeadsetTypes;
  typeCode: number;
  name: string;
}
export interface HeadsetDetectionEvent {
  isConnected: boolean;
  device: HeadsetDevice | null;
}

export interface HeadsetDetectionPlugin {
  /**
   * Will start listening to Headphone device changes.
   *
   * IMPORTANT: Call this *after* you set your listeners.
   */
  start(): void;

  /**
   * Each time the devices change this event will be triggered.
   *
   * It also will be triggered one time after {@link start} is called.
   *
   * IMPORTANT: Register your listeners *before* you call {@link start}
   */
  addListener(
    eventName: HeadphoneDetectionEventNames.ConnectedHeadphones,
    listenerFunc: (info: HeadsetDetectionEvent) => void,
  ): PluginListenerHandle;
}
