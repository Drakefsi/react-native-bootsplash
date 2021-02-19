declare module "react-native-bootsplash" {
  export type ShowConfig = {
    duration?: number;
  };
  export type HideConfig = {
    duration?: number;
  };
  export type VisibilityStatus = "visible" | "hidden" | "transitioning";

  export function show(config?: ShowConfig): void;
  export function hide(config?: HideConfig): void;
  export function getVisibilityStatus(): Promise<VisibilityStatus> {
    return NativeModule.getVisibilityStatus();
  }

  const RNBootSplash: {
    show: typeof show;
    hide: typeof hide;
    getVisibilityStatus: () => Promise<VisibilityStatus>;
  };

  export default RNBootSplash;
}
