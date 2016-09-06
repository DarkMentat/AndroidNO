package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.ar25.androidno.entities.Post.*;

public class FakeNOPostsApi implements NOPostsApi {

  private final ArrayList<Post> mFakePosts;

  public FakeNOPostsApi() {
    mFakePosts = new ArrayList<>();
    mFakePosts.add(newPost(0L, "Test post 1", "001", "http://pp.vk.me/c627130/v627130524/d0b2/TPRLR-PWSVo.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate."));
    mFakePosts.add(newPost(1L, "Test post 2", "002", "http://pp.vk.me/c624621/v624621448/1e79e/poETQJ4kCyE.jpg", "Morbi consectetur, nunc ut luctus suscipit, lacus ante venenatis lorem, non fermentum dui purus non sapien. Nullam auctor tempus quam, non volutpat metus luctus interdum. Praesent pretium ante libero, eu gravida eros porta vitae. Duis semper dapibus odio, eget fermentum lectus ullamcorper quis. Praesent vulputate, risus feugiat pulvinar lobortis, tortor ex consequat ligula, eu consectetur dui nisi vitae ipsum. Donec consectetur commodo eros ut aliquet. Aliquam suscipit ac dui ac sodales. Suspendisse dictum nulla sit amet vulputate fermentum. Nulla porttitor, leo non sodales scelerisque, nulla lacus lacinia erat, eu aliquam massa urna in leo. Suspendisse potenti. Nullam finibus purus nec tortor feugiat fermentum. Phasellus laoreet at tortor euismod iaculis."));
    mFakePosts.add(newPost(2L, "Test post 3", "003", "http://pp.vk.me/c9568/v9568282/921/7OInBxko54U.jpg", "Suspendisse ac sapien maximus, interdum magna vitae, pharetra ex. Nam eget lorem ac enim aliquet malesuada sed sed ipsum. Sed at nisl neque. Nullam eu mauris metus. Maecenas vulputate leo dapibus risus convallis, quis commodo justo aliquet. Praesent egestas, sapien vitae mollis aliquet, velit odio finibus eros, in vulputate turpis risus et tortor. Donec vel lorem vitae massa facilisis finibus."));
    mFakePosts.add(newPost(3L, "Test post 4", "004", "http://pp.vk.me/c308617/v308617239/5826/Dog9X_cK31Q.jpg", "Sed in finibus tortor. Aliquam vulputate sodales orci quis mattis. Proin eleifend massa metus, non venenatis lacus consequat a. Sed aliquet ullamcorper tortor sit amet fermentum. Etiam rutrum tempor blandit. Fusce pretium non metus nec viverra. Phasellus porta feugiat magna nec ultrices. Mauris ut neque a felis facilisis dictum. Morbi vitae fermentum nulla, nec accumsan dui."));
    mFakePosts.add(newPost(4L, "Test post 5", "005", "http://pp.vk.me/c625524/v625524333/13b37/SpEz0XoMEgQ.jpg", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa."));
  }

  @Override public Observable<List<Post>> getLastPosts() {
    return Observable.just(mFakePosts);
  }

  public ArrayList<Post> getFakePosts() {
    return mFakePosts;
  }
}
