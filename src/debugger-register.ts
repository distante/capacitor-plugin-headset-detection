
interface MyWindowWihtOptionalSaninnNamespace extends Window {
  Saninn?: {
    [key in DebugObjectCategoryNames]?: any
  }
}

enum DebugObjectCategoryNames {
  CapacitorPlugins = 'CapacitorPlugins'
}

type DebugObject<T> =
  T extends DebugObjectCategoryNames.CapacitorPlugins ? Required<MyWindowWihtOptionalSaninnNamespace> & { Saninn: { [DebugObjectCategoryNames.CapacitorPlugins]: {} } } :
  never;

declare let window: MyWindowWihtOptionalSaninnNamespace;

function getWindowWithSaninnNameSpace(): Required<MyWindowWihtOptionalSaninnNamespace> {
  if (!window.Saninn) {
    window.Saninn = {}
  }

  return window as Required<MyWindowWihtOptionalSaninnNamespace>
}

function getWindowWithCategory<T extends DebugObjectCategoryNames>(category: DebugObjectCategoryNames): DebugObject<T> {
  const myWindow = getWindowWithSaninnNameSpace();

  if (!myWindow.Saninn[category]) {
    myWindow.Saninn[category] = {}
  }

  return myWindow as DebugObject<T>;

}

export function registerCapacitorPlugin(name: string, value: any): void {

  let myWindow = getWindowWithCategory(DebugObjectCategoryNames.CapacitorPlugins);
  
  myWindow.Saninn.CapacitorPlugins[name] = value;
}