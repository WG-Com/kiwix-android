package org.kiwix.kiwixmobile.testutils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.kiwix.kiwixmobile.utils.NetworkUtils.parseURL;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.kiwix.kiwixmobile.library.entity.LibraryNetworkEntity.Book;

/**
 * Created by mhutti1 on 07/04/17.
 */

public class TestUtils {
  public static boolean hasStoragePermission() {
    return ContextCompat.checkSelfPermission(InstrumentationRegistry.getTargetContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(InstrumentationRegistry.getTargetContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
  }

  public static void allowPermissionsIfNeeded() {
    if (Build.VERSION.SDK_INT >= 23 && !hasStoragePermission()) {
      UiDevice device = UiDevice.getInstance(getInstrumentation());
      UiObject allowPermissions = device.findObject(new UiSelector().clickable(true).checkable(false).index(1));
      if (allowPermissions.exists()) {
        try {
          allowPermissions.click();
        } catch (UiObjectNotFoundException e) {}
      }
    }
  }

  public static Matcher<Object> withContent(final String content) {
    return new BoundedMatcher<Object, Book>(Book.class) {
      @Override
      public boolean matchesSafely(Book myObj) {
        if (myObj.getUrl() != null) {
          return myObj.getUrl().contains(content);
        } else {
          return myObj.file.getPath().contains(content);
        }
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with content '" + content + "'");
      }
    };
  }
}
