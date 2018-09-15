package presenter;

import java.util.List;

import models.Variables;

public interface VolleyCallback {
    void onSuccess(String result);
    void onFailure(String result);
    List<Variables.Product> onAutoComplete(String result);
}
