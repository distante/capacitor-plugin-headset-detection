declare module '@capacitor/core' {
  interface PluginRegistry {
    Restart: RestartPlugin;
  }
}

export interface RestartPlugin {
  restart(): Promise<void>;
}
