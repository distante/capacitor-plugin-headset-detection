import { PluginListenerHandle } from '@capacitor/core';

declare module '@capacitor/core' {
  interface PluginRegistry {
    Restart: RestartPlugin;
  }
}

export interface HeadsetDevice {
  id: number;
  type: string;
  typeCode: number;
  name: string;
}
export interface HeadsetDetectionEvent {
  isConnected: boolean;
  devices: HeadsetDevice[];
}

export interface RestartPlugin {
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
