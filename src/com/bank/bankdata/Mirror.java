package com.bank.bankdata;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Fx;
import com.bank.tools.Dynatuple;
import java.util.ArrayList;
import java.util.List;

/**
 * Its existence is to ensure there is a function that maps T to each of Image[T] and is one-to-one,
 * You can consider this class as a function of type of : T -> Image[T].
 * 
 * @author jinende
 *
 * @param <T> Representing the type T can be serializable.
 */
public abstract class Mirror<T> {
  // T -> Image[T]
  public abstract Image<T> injective(T t) throws InternalException, InteractionException;

  // The reason we can cross platform
  private static class DynatupleImage<Q, S> implements Image<Dynatuple<Q, S>> {

    // The following two field actually means Dynatupel<Image<Q>, Image<S>> = Image<Dynatuple<Q, S>>


    private static final long serialVersionUID = 409591804513081026L;

    // left field
    private Image<Q> imageLeft;
    // right field
    private Image<S> imageRight;

    // Package level, a constructor which is not supposed to be used by invoker
    DynatupleImage(Image<Q> imageQ, Image<S> imageS)
        throws InternalException, InteractionException {
      this.imageLeft = imageQ;
      this.imageRight = imageS;
    }

    @Override
    public Dynatuple<Q, S> injective() throws InternalException, InteractionException {
      Q q = this.imageLeft.injective();
      S s = this.imageRight.injective();
      return new Dynatuple<>(q, s);
    }
  }

  /**
   * It is just a trivial construction of a function with type: Mirror[Q] -> Mirror[S] ->
   * Mirror[Tuple[Q, S]] It means if there is a function that can make Q and S into an image, then I
   * can create a trivial function which can make Tuple[Q, S] into an image.
   * 
   * @param mirrorLeft The evidence that Q can be made into image
   * @param mirrorRight The evidence that S can be made into image
   * @return The fact that Tuple[Q, S] can be made into image
   */
  public static <Q, S> Mirror<Dynatuple<Q, S>>
      combineMirror(Mirror<Q> mirrorLeft, Mirror<S> mirrorRight) {


    // Anonymous Class, with type Mirror
    return new Mirror<Dynatuple<Q, S>>() {
      @Override
      public Image<Dynatuple<Q, S>> injective(Dynatuple<Q, S> t)
          throws InternalException, InteractionException {
        return new DynatupleImage<Q, S>(mirrorLeft.injective(t.left()),
            mirrorRight.injective(t.right()));
      }

    };

  }

  /**
   * A trivial mapping allowing two exception invoked.
   * 
   * @param orgList The mapped list
   * @param mapping The mapping function
   * @return A list after mapping
   * @throws InternalException If unexpected happen
   * @throws InteractionException If unexpected input
   */
  private static <Q, S> ArrayList<Q> mapWithException(List<S> orgList, Fx<S, Q> mapping)
      throws InternalException, InteractionException {
    ArrayList<Q> retList = new ArrayList<Q>(orgList.size());
    for (S eachOrg : orgList) {
      retList.add(mapping.fx(eachOrg));
    }
    return retList;
  }

  private static class ListImage<Q> implements Image<List<Q>> {

    private static final long serialVersionUID = 2056771561504321490L;
    private ArrayList<Image<Q>> images;

    ListImage(ArrayList<Image<Q>> images) {
      this.images = images;
    }

    @Override
    public List<Q> injective() throws InternalException, InteractionException {
      return mapWithException(this.images, x -> x.injective());
    }
  }

  /**
   * It is just a trivial construction of a function with type: Mirror[Q] -> Mirror[List[Q]] It
   * means if there is a function that can make Q into an image, then I can create a trivial
   * function which can make List[Q] into an image.
   * 
   * @param mirror the ability to make Q into image
   * @return The ability to make List[Q] into image
   */
  public static <Q> Mirror<List<Q>> duplicateMirror(Mirror<Q> mirror) {
    // Anonymous Class, with type Mirror, Reason:
    // 1. clean namespace: I don't need to bother to give name to those class I use only one time
    // 2. Type restriction : I don't need to bother with the implementation details of those class,
    // they can only have method specified in interface/abstract class
    return new Mirror<List<Q>>() {

      @Override
      public Image<List<Q>> injective(List<Q> t)
          throws InternalException, InteractionException {
        // List<Image<Q>> -> Image<List<Q>>
        return new ListImage<Q>(mapWithException(t, x -> mirror.injective(x)));

      }


    };


  }

}
