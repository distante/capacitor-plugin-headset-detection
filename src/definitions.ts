import { PluginListenerHandle } from '@capacitor/core';

declare module '@capacitor/core' {
  interface PluginRegistry {
    HeadsetDetection: HeadsetDetectionPlugin;
  }
}

export enum HeadsetTypes {
  BLUETOOTH_A2DP = 'TYPE_BLUETOOTH_A2DP',
  AUX_LINE = 'TYPE_AUX_LINE',
  WIRED_HEADPHONES = 'TYPE_WIRED_HEADPHONES',
  WIRED_HEADSET = 'TYPE_WIRED_HEADSET',
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
    eventName: 'ConnectedHeadphones',
    listenerFunc: (info: HeadsetDetectionEvent) => void,
  ): PluginListenerHandle;
}
