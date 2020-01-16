#import <React/RCTViewManager.h>
#import "RnCastButton.h"

@interface RnCastButtonManager : RCTViewManager
@end

@implementation RnCastButtonManager

RCT_EXPORT_MODULE()

- (UIView *)view {
  return [[RnCastButton alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(tintColor, UIColor)

@end