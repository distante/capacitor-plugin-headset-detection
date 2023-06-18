import { WebPlugin } from '@capacitor/core';

import { registerCapacitorPlugin } from './debugger-register';
import { HeadsetDetectionEvent, HeadsetDetectionPlugin } from './definitions';
import { HeadphoneDetectionEventNames, HeadsetTypes } from './models';

const DEVICE_TEST_OBJECTS: { [key: string]: HeadsetDetectionEvent } = {
  bluetooth: {
    isConnected: true,
    device: {
      id: 1,
      type: HeadsetTypes.BLUETOOTH_A2DP,
      typeCode: 1,
      name: HeadsetTypes.BLUETOOTH_A2DP,
    },
  },
  wiredHeadphones: {
    isConnected: true,
    device: {
      id: 2,
      type: HeadsetTypes.WIRED_HEADPHONES,
      typeCode: 2,
      name: HeadsetTypes.WIRED_HEADPHONES,
    },
  },
  wiredHeadset: {
    isConnected: true,
    device: {
      id: 3,
      type: HeadsetTypes.WIRED_HEADSET,
      typeCode: 3,
      name: HeadsetTypes.WIRED_HEADSET,
    },
  },
  auxLine: {
    isConnected: true,
    device: {
      id: 4,
      type: HeadsetTypes.AUX_LINE,
      typeCode: 4,
      name: HeadsetTypes.AUX_LINE,
    },
  },
  none: {
    isConnected: false,
    device: null,
  },
};
export class HeadsetDetectionWeb extends WebPlugin implements HeadsetDetectionPlugin {
  constructor() {
    super({
      name: 'HeadsetDetection',
      platforms: ['web'],
    });

    this.registerDebugFunctions();
  }

  start(): void {
    console.log('HeadsetDetectionPlugin starting mocked web implementation');
    if (this.listeners[HeadphoneDetectionEventNames.ConnectedHeadphones].length) {
      this.listeners[HeadphoneDetectionEventNames.ConnectedHeadphones].forEach((listener) => {
        listener(DEVICE_TEST_OBJECTS.none);
      });
    }
  }

  private registerDebugFunctions(): void {
    const functions: Array<{ name: string; theFunction: Function }> = [];

    Object.keys(DEVICE_TEST_OBJECTS).forEach((deviceTestObjectName) => {
      functions.push({
        name: `emit_update_with_device_type_${deviceTestObjectName}`,
        theFunction: () => {
          if (!this.listeners[HeadphoneDetectionEventNames.ConnectedHeadphones].length) {
            return;
          }
          this.listeners[HeadphoneDetectionEventNames.ConnectedHeadphones].forEach((listener) => {
            listener(DEVICE_TEST_OBJECTS[deviceTestObjectName]);
          });
        },
      });
    });

    const objectToRegister = functions.reduce((finalObject, functionObject) => {
      finalObject[functionObject.name] = functionObject.theFunction;

      return finalObject;
    }, Object.create(null));

    registerCapacitorPlugin('HeadsetDetection', objectToRegister);
  }
}
